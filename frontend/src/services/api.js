import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
const ANALYTICS_API_URL = process.env.REACT_APP_ANALYTICS_URL || 'http://localhost:8081/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const analyticsApi = axios.create({
  baseURL: ANALYTICS_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const fetchCostTrend = async (days = 30) => {
  const response = await analyticsApi.get(`/analytics/cost/trend?days=${days}`);
  return response.data;
};

export const fetchDailyUsage = async (customerId, startDate, endDate) => {
  const response = await analyticsApi.get('/analytics/usage/daily', {
    params: { customerId, startDate, endDate },
  });
  return response.data;
};

export const fetchBillingRecords = async (customerId, page = 0, size = 10) => {
  const response = await api.get(`/billing/customer/${customerId}`, {
    params: { page, size },
  });
  return response.data;
};

export const fetchCustomerSummary = async () => {
  // Mock data for demo
  return {
    totalRevenue: 245830,
    activeCustomers: 1247,
    avgQueryTime: 120,
    eventsPerMinute: 125450,
  };
};

export const ingestUsageEvent = async (event) => {
  const response = await api.post('/billing/usage', event);
  return response.data;
};

export default api;
