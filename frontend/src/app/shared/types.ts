export interface PlantItem {
  id: number;
  name: string;
  grade: string;
  quantity: number;
  unit: string;
}

export interface BidResponse {
  id: number;
  createdAt: string;
  totalPrice: number;
  estimatedDeliveryDate: string;
  notes: string;
  supplier: Supplier;
  status: BidResponseStatus;
}

export interface BidRequest {
  id: number;
  createdAt: string;
  projectName: string;
  requiredBy: string;
  landscaper: Landscaper;
  plantItems: PlantItem[];
  status: BidRequestStatus;
  bidResponses: BidResponse[];
}

export interface Landscaper {
  id: number;
  name: string;
}

export interface Supplier {
  id: number;
  name: string;
}

export enum BidRequestStatus {
  OPEN = 'OPEN',
  IN_PROGRESS = 'IN_PROGRESS',
  AWARDED = 'AWARDED',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED'
}

export enum BidResponseStatus {
  SUBMITTED = 'SUBMITTED',
  AWARDED = 'AWARDED',
  REJECTED = 'REJECTED',
  WITHDRAWN = 'WITHDRAWN'
}

export enum ProfileType {
  SUPPLIER = 'SUPPLIER',
  LANDSCAPER = 'LANDSCAPER'
}
