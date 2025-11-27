import React from 'react';
import { Grid, Paper, Typography, Box } from '@mui/material';
import { useQuery } from 'react-query';
import {
  LineChart,
  Line,
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';
import { fetchCostTrend, fetchCustomerSummary } from '../services/api';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

const Dashboard = () => {
  const { data: costTrend } = useQuery('costTrend', () => fetchCostTrend(30));
  const { data: customerSummary } = useQuery('customerSummary', fetchCustomerSummary);

  const trendData = costTrend ? costTrend.labels.map((label, i) => ({
    date: label,
    cost: costTrend.values[i],
  })) : [];

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Billing Dashboard Overview
      </Typography>
      
      <Grid container spacing={3}>
        {/* Summary Cards */}
        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center' }}>
            <Typography variant="h6" color="textSecondary">
              Total Revenue
            </Typography>
            <Typography variant="h4" color="primary">
              $245,830
            </Typography>
          </Paper>
        </Grid>
        
        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center' }}>
            <Typography variant="h6" color="textSecondary">
              Active Customers
            </Typography>
            <Typography variant="h4" color="primary">
              1,247
            </Typography>
          </Paper>
        </Grid>
        
        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center' }}>
            <Typography variant="h6" color="textSecondary">
              Avg. Query Time
            </Typography>
            <Typography variant="h4" color="success.main">
              120ms
            </Typography>
            <Typography variant="caption" color="textSecondary">
              30% faster
            </Typography>
          </Paper>
        </Grid>
        
        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2, textAlign: 'center' }}>
            <Typography variant="h6" color="textSecondary">
              Events/min
            </Typography>
            <Typography variant="h4" color="primary">
              125,450
            </Typography>
          </Paper>
        </Grid>

        {/* Cost Trend Chart */}
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              30-Day Cost Trend
            </Typography>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={trendData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line
                  type="monotone"
                  dataKey="cost"
                  stroke="#8884d8"
                  strokeWidth={2}
                  name="Daily Cost ($)"
                />
              </LineChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>

        {/* Resource Distribution */}
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Resource Distribution
            </Typography>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={[
                    { name: 'Compute', value: 400 },
                    { name: 'Storage', value: 300 },
                    { name: 'Network', value: 200 },
                    { name: 'Database', value: 100 },
                  ]}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={(entry) => entry.name}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {COLORS.map((color, index) => (
                    <Cell key={`cell-${index}`} fill={color} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>

        {/* Monthly Revenue */}
        <Grid item xs={12}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Monthly Revenue Breakdown
            </Typography>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart
                data={[
                  { month: 'Jan', revenue: 45000, usage: 35000 },
                  { month: 'Feb', revenue: 52000, usage: 42000 },
                  { month: 'Mar', revenue: 48000, usage: 38000 },
                  { month: 'Apr', revenue: 61000, usage: 51000 },
                  { month: 'May', revenue: 55000, usage: 45000 },
                  { month: 'Jun', revenue: 67000, usage: 57000 },
                ]}
              >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="revenue" fill="#8884d8" name="Revenue ($)" />
                <Bar dataKey="usage" fill="#82ca9d" name="Usage Cost ($)" />
              </BarChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard;
