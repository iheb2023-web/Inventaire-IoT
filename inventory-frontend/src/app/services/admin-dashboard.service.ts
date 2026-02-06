import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject, of } from 'rxjs';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client/dist/sockjs';
import { DashboardData } from '../models/dashboard-data';
import { Product, ProductWithStock, ProductRegisterRequest, RfidWsMessage } from '../models/product';

export interface DashboardStats {
  totalProducts: number;
  totalStock: number;
  totalStoreStock: number;
}

export interface RfidEvent {
  id: number;
  productId: number;
  eventType: 'ENTRY' | 'EXIT';
  location: 'STOCK' | 'STORE';
  esp32Id: string;
  createdAt: string;
}

export interface RfidEventWithProduct extends RfidEvent {
  productName: string;
}

@Injectable({
  providedIn: 'root'
})
export class AdminDashboardService {
  private readonly http = inject(HttpClient);
  private readonly rfidEventsSubject = new Subject<RfidWsMessage>();
  private readonly apiBaseUrl = 'http://localhost:8080';
  private stompClient: Client | null = null;
  private connected = false;

  readonly rfidEvents$ = this.rfidEventsSubject.asObservable();

  getDashboardData(): Observable<DashboardData | null> {
    return of(null);
  }

  getStats(): Observable<{ success: boolean; message: string; data: DashboardStats }> {
    return this.http.get<{ success: boolean; message: string; data: DashboardStats }>(`${this.apiBaseUrl}/api/rfid/stats`);
  }

  getRecentEvents(limit: number = 20): Observable<{ success: boolean; message: string; data: RfidEvent[] }> {
    return this.http.get<{ success: boolean; message: string; data: RfidEvent[] }>(`${this.apiBaseUrl}/api/rfid/events/recent?limit=${limit}`);
  }

  getRecentEventsWithProduct(limit: number = 20): Observable<{ success: boolean; message: string; data: RfidEventWithProduct[] }> {
    return this.http.get<{ success: boolean; message: string; data: RfidEventWithProduct[] }>(`${this.apiBaseUrl}/api/rfid/events/recent-with-product?limit=${limit}`);
  }

  getStoreStats() {
    // TODO: Récupérer les statistiques des magasins
  }

  getInventoryStats() {
    // TODO: Récupérer les statistiques d'inventaire
  }

  getProducts(): Observable<{ success: boolean; message: string; data: Product[] }> {
    return this.http.get<{ success: boolean; message: string; data: Product[] }>(`${this.apiBaseUrl}/api/products`);
  }

  getProductsWithStock(): Observable<{ success: boolean; message: string; data: ProductWithStock[] }> {
    return this.http.get<{ success: boolean; message: string; data: ProductWithStock[] }>(`${this.apiBaseUrl}/api/products/with-stock`);
  }

  registerProduct(payload: ProductRegisterRequest): Observable<Product> {
    return this.http.post<Product>(`${this.apiBaseUrl}/api/products`, payload);
  }

  updateProduct(id: number, payload: ProductRegisterRequest): Observable<Product> {
    return this.http.put<Product>(`${this.apiBaseUrl}/api/products/${id}`, payload);
  }

  deleteProduct(id: number): Observable<{ success: boolean; message: string }> {
    return this.http.delete<{ success: boolean; message: string }>(`${this.apiBaseUrl}/api/products/${id}`);
  }

  deleteEvent(id: number): Observable<{ success: boolean; message: string }> {
    return this.http.delete<{ success: boolean; message: string }>(`${this.apiBaseUrl}/api/rfid/${id}`);
  }

  connectRfidWs(): void {
    if (this.connected || this.stompClient) {
      return;
    }

    const client = new Client({
      reconnectDelay: 5000,
      webSocketFactory: () => new SockJS(`${this.apiBaseUrl}/ws`)
    });

    client.onConnect = () => {
      this.connected = true;
      client.subscribe('/topic/rfid', message => this.handleRfidMessage(message));
    };

    client.onDisconnect = () => {
      this.connected = false;
    };

    client.onStompError = () => {
      this.connected = false;
    };

    this.stompClient = client;
    client.activate();
  }

  private handleRfidMessage(message: IMessage): void {
    if (!message.body) {
      return;
    }

    try {
      const payload = JSON.parse(message.body) as RfidWsMessage;
      if (payload?.type && payload?.rfidTag) {
        this.rfidEventsSubject.next(payload);
      }
    } catch {
      // Ignore invalid payloads
    }
  }
}
