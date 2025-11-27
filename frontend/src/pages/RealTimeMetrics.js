import React, { useEffect, useState } from 'react';
import {
  Box,
  Paper,
  Typography,
  Grid,
  Card,
  CardContent,
} from '@mui/material';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from 'recharts';

const RealTimeMetrics = () => {
  const [metrics, setMetrics] = useState({
    throughput: [],
    latency: [],
    errorRate: [],
  });

  useEffect(() => {
    const interval = setInterval(() => {
      const now = new Date().toLocaleTimeString();
      
      setMetrics((prev) => ({
        throughput: [
          ...prev.throughput.slice(-19),
          { time: now, value: Math.floor(Math.random() * 5000) + 95000 },
        ],
        latency: [
          ...prev.latency.slice(-19),
          { time: now, value: Math.floor(Math.random() * 50) + 100 },
        ],
        errorRate: [
          ...prev.errorRate.slice(-19),
          { time: now, value: Math.random() * 0.5 },
        ],
      }));
    }, 2000);

    return () => clearInterval(interval);
  }, []);

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Real-Time System Metrics
      </Typography>

      <Grid container spacing={3}>
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6" color="textSecondary">
                Current Throughput
              </Typography>
              <Typography variant="h3" color="primary">
                {metrics.throughput.length > 0
                  ? metrics.throughput[metrics.throughput.length - 1].value.toLocaleString()
                  : '0'}
              </Typography>
              <Typography variant="caption" color="textSecondary">
                events/minute
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6" color="textSecondary">
                P95 Latency
              </Typography>
              <Typography variant="h3" color="success.main">
                {metrics.latency.length > 0
                  ? metrics.latency[metrics.latency.length - 1].value
                  : '0'}
                ms
              </Typography>
              <Typography variant="caption" color="textSecondary">
                30% improvement
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6" color="textSecondary">
                Error Rate
              </Typography>
              <Typography variant="h3" color="error.main">
                {metrics.errorRate.length > 0
                  ? metrics.errorRate[metrics.errorRate.length - 1].value.toFixed(3)
                  : '0'}
                %
              </Typography>
              <Typography variant="caption" color="textSecondary">
                last minute
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Throughput (Events/min)
            </Typography>
            <ResponsiveContainer width="100%" height={250}>
              <LineChart data={metrics.throughput}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="time" />
                <YAxis />
                <Tooltip />
                <Line
                  type="monotone"
                  dataKey="value"
                  stroke="#1976d2"
                  strokeWidth={2}
                  dot={false}
                />
              </LineChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>

        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Query Latency (ms)
            </Typography>
            <ResponsiveContainer width="100%" height={250}>
              <LineChart data={metrics.latency}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="time" />
                <YAxis />
                <Tooltip />
                <Line
                  type="monotone"
                  dataKey="value"
                  stroke="#2e7d32"
                  strokeWidth={2}
                  dot={false}
                />
              </LineChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>

        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Error Rate (%)
            </Typography>
            <ResponsiveContainer width="100%" height={250}>
              <LineChart data={metrics.errorRate}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="time" />
                <YAxis />
                <Tooltip />
                <Line
                  type="monotone"
                  dataKey="value"
                  stroke="#d32f2f"
                  strokeWidth={2}
                  dot={false}
                />
              </LineChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default RealTimeMetrics;
