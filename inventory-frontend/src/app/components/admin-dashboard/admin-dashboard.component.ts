import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnInit,
  inject,
  signal,
  computed
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AdminDashboardService, DashboardStats, RfidEvent, RfidEventWithProduct } from '../../services/admin-dashboard.service';
import { DashboardData } from '../../models/dashboard-data';
import { Product, ProductWithStock, ProductRegisterRequest, RfidWsMessage } from '../../models/product';

@Component({
  selector: 'app-admin-dashboard',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminDashboardComponent implements OnInit {
  private readonly dashboardService = inject(AdminDashboardService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly formBuilder = inject(FormBuilder);

  readonly dashboardData = signal<DashboardData | null>(null);
  readonly stats = signal<DashboardStats | null>(null);
  readonly recentEvents = signal<RfidEventWithProduct[]>([]);
  readonly isLoadingStats = signal(false);
  readonly isLoadingEvents = signal(false);

  readonly products = signal<ProductWithStock[]>([]);
  readonly isLoadingProducts = signal(false);
  readonly isLoadingDelete = signal<number | null>(null);
  readonly isLoadingDeleteEvent = signal<number | null>(null);

  readonly isFormOpen = signal(false);
  readonly isSaving = signal(false);
  readonly errorMessage = signal<string | null>(null);
  readonly successMessage = signal<string | null>(null);
  readonly currentLocation = signal<'STOCK' | 'STORE' | null>(null);
  readonly formMode = signal<'new_rfid' | 'add_product' | 'edit_product'>('add_product');
  readonly editingProductId = signal<number | null>(null);

  readonly selectedView = signal<'home' | 'stock' | 'store' | 'products'>('home');

  readonly productForm = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(120)]],
    barcode: ['', [Validators.required, Validators.maxLength(64)]],
    rfidTag: ['', [Validators.required, Validators.maxLength(128)]],
    description: ['', [Validators.maxLength(500)]],
    unitWeight: [0.001, [Validators.required, Validators.min(0.001)]]
  });

  ngOnInit(): void {
    this.loadDashboardData();
    this.dashboardService.connectRfidWs();
    this.dashboardService.rfidEvents$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(event => this.handleRfidEvent(event));
  }

  loadDashboardData(): void {
    this.dashboardService
      .getDashboardData()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(data => this.dashboardData.set(data));

    this.loadStats();
    this.loadRecentEvents();
  }

  loadStats(): void {
    this.isLoadingStats.set(true);
    this.dashboardService
      .getStats()
      .pipe(
        finalize(() => this.isLoadingStats.set(false)),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: (response) => {
          this.stats.set(response.data);
        },
        error: (err) => {
          console.error('Error loading stats:', err);
        }
      });
  }

  loadRecentEvents(): void {
    this.isLoadingEvents.set(true);
    this.dashboardService
      .getRecentEventsWithProduct(15)
      .pipe(
        finalize(() => this.isLoadingEvents.set(false)),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: (response) => {
          this.recentEvents.set(response.data);
        },
        error: (err) => {
          console.error('Error loading events:', err);
        }
      });
  }

  handleRfidEvent(event: RfidWsMessage): void {
    if (event.type === 'NEW_PRODUCT' && event.location === 'STOCK') {
      this.openProductForm(event.rfidTag, event.location);
    }
    // Reload events after RFID event
    this.loadRecentEvents();
  }

  openProductForm(rfidTag: string, location: 'STOCK' | 'STORE'): void {
    this.errorMessage.set(null);
    this.successMessage.set(null);
    this.currentLocation.set(location);
    this.formMode.set('new_rfid');
    this.editingProductId.set(null);
    this.productForm.reset({
      name: '',
      barcode: '',
      rfidTag,
      description: '',
      unitWeight: 0.001
    });
    this.isFormOpen.set(true);
  }

  openAddProductForm(): void {
    this.errorMessage.set(null);
    this.successMessage.set(null);
    this.currentLocation.set(null);
    this.formMode.set('add_product');
    this.editingProductId.set(null);
    this.productForm.reset({
      name: '',
      barcode: '',
      rfidTag: '',
      description: '',
      unitWeight: 0.001
    });
    this.isFormOpen.set(true);
  }

  openEditProductForm(product: Product): void {
    this.errorMessage.set(null);
    this.successMessage.set(null);
    this.currentLocation.set(null);
    this.formMode.set('edit_product');
    this.editingProductId.set(product.id);
    this.productForm.reset({
      name: product.name,
      barcode: product.barcode,
      rfidTag: product.rfidTag,
      description: product.description,
      unitWeight: product.unitWeight
    });
    this.isFormOpen.set(true);
  }

  closeProductForm(): void {
    this.isFormOpen.set(false);
  }

  submitProduct(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    const rawValue = this.productForm.getRawValue();
    const payload: ProductRegisterRequest = {
      name: rawValue.name.trim(),
      barcode: rawValue.barcode.trim(),
      rfidTag: rawValue.rfidTag.trim(),
      description: rawValue.description?.trim() || '',
      unitWeight: Number(rawValue.unitWeight),
      esp32Id: 'ESP32_STOCK'
    };

    console.log('Sending payload:', JSON.stringify(payload, null, 2));

    this.isSaving.set(true);
    this.errorMessage.set(null);
    this.successMessage.set(null);

    if (this.formMode() === 'edit_product' && this.editingProductId()) {
      this.dashboardService
        .updateProduct(this.editingProductId()!, payload)
        .pipe(finalize(() => this.isSaving.set(false)))
        .subscribe({
          next: () => {
            this.successMessage.set('Produit modifié avec succès!');
            setTimeout(() => {
              this.closeProductForm();
              this.loadProducts();
            }, 1500);
          },
          error: (err) => {
            console.error('Error updating product:', err);
            this.errorMessage.set(
              err.error?.message || 'Erreur lors de la modification du produit'
            );
          }
        });
    } else {
      this.dashboardService
        .registerProduct(payload)
        .pipe(finalize(() => this.isSaving.set(false)))
        .subscribe({
          next: (response) => {
            console.log('Product created:', response);
            this.successMessage.set('Produit enregistré avec succès!');
            setTimeout(() => {
              this.closeProductForm();
              this.loadStats();
              this.loadRecentEvents();
              if (this.selectedView() === 'products') {
                this.loadProducts();
              }
            }, 1500);
          },
          error: (err) => {
            console.error('Product registration error:', err);
            console.error('Error details:', {
              status: err?.status,
              statusText: err?.statusText,
              message: err?.error?.message,
              body: err?.error
            });
            const errorMsg = err?.error?.message || err?.statusText || 'Erreur lors de l\'enregistrement du produit';
            this.errorMessage.set(errorMsg);
          }
        });
    }
  }

  loadProducts(): void {
    this.isLoadingProducts.set(true);
    this.dashboardService
      .getProductsWithStock()
      .pipe(
        finalize(() => this.isLoadingProducts.set(false)),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: (response) => {
          this.products.set(response.data);
        },
        error: (err) => {
          console.error('Error loading products:', err);
        }
      });
  }

  deleteProduct(product: Product): void {
    if (!confirm(`Êtes-vous sûr de vouloir supprimer le produit "${product.name}"?`)) {
      return;
    }

    this.isLoadingDelete.set(product.id);
    this.dashboardService
      .deleteProduct(product.id)
      .pipe(
        finalize(() => this.isLoadingDelete.set(null)),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: () => {
          this.successMessage.set('Produit supprimé avec succès!');
          this.loadProducts();
          this.loadStats();
          setTimeout(() => this.successMessage.set(null), 3000);
        },
        error: (err) => {
          console.error('Error deleting product:', err);
          this.errorMessage.set(
            err.error?.message || 'Erreur lors de la suppression du produit'
          );
          setTimeout(() => this.errorMessage.set(null), 3000);
        }
      });
  }

  deleteEvent(event: RfidEvent): void {
    if (!confirm(`Êtes-vous sûr de vouloir supprimer cet événement?`)) {
      return;
    }

    this.isLoadingDeleteEvent.set(event.id);
    this.dashboardService
      .deleteEvent(event.id)
      .pipe(
        finalize(() => this.isLoadingDeleteEvent.set(null)),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe({
        next: () => {
          this.loadRecentEvents();
          this.loadStats();
        },
        error: (err) => {
          console.error('Error deleting event:', err);
          alert(err.error?.message || 'Erreur lors de la suppression de l\'événement');
        }
      });
  }

  getEventIcon(eventType: string, location: string): string {
    if (eventType === 'ENTRY') {
      return location === 'STOCK' ? '📥' : '📦';
    }
    return location === 'STOCK' ? '📤' : '🛒';
  }

  getEventLabel(eventType: string, location: string): string {
    if (eventType === 'ENTRY') {
      return location === 'STOCK' ? 'Entrée Stock' : 'Entrée Magasin';
    }
    return location === 'STOCK' ? 'Sortie Stock' : 'Sortie Magasin';
  }

  selectView(view: 'home' | 'stock' | 'store' | 'products'): void {
    this.selectedView.set(view);
    if (view !== 'home') {
      this.loadRecentEvents();
    }
    if (view === 'products') {
      this.loadProducts();
    }
  }

  goBack(): void {
    this.selectedView.set('home');
  }
}
