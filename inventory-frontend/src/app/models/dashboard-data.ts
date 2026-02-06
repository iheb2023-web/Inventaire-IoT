export interface DashboardData {
  totalProducts: number;
  totalStores: number;
  lowStockItems: number;
  recentTransactions: Transaction[];
}

export interface Transaction {
  id: string;
  date: Date;
  type: 'in' | 'out';
  quantity: number;
  product: string;
  store: string;
}
