import React, { useState } from 'react';
import {
  Box,
  Paper,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  Chip,
  TextField,
  Button,
} from '@mui/material';
import { useQuery } from 'react-query';
import { fetchBillingRecords } from '../services/api';

const BillingRecords = () => {
  const [customerId, setCustomerId] = useState('550e8400-e29b-41d4-a716-446655440000');
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const { data: billingData } = useQuery(
    ['billingRecords', customerId, page, rowsPerPage],
    () => fetchBillingRecords(customerId, page, rowsPerPage)
  );

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'PAID':
        return 'success';
      case 'PENDING':
        return 'warning';
      case 'OVERDUE':
        return 'error';
      default:
        return 'default';
    }
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Billing Records
      </Typography>

      <Paper sx={{ p: 2, mb: 3 }}>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <TextField
            label="Customer ID"
            value={customerId}
            onChange={(e) => setCustomerId(e.target.value)}
            sx={{ flexGrow: 1 }}
          />
          <Button variant="contained">Search</Button>
        </Box>
      </Paper>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Invoice #</TableCell>
              <TableCell>Customer</TableCell>
              <TableCell>Billing Period</TableCell>
              <TableCell align="right">Amount</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Due Date</TableCell>
              <TableCell>Paid Date</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {billingData?.content?.map((record) => (
              <TableRow key={record.id}>
                <TableCell>{record.invoice_number}</TableCell>
                <TableCell>{record.customer_name}</TableCell>
                <TableCell>
                  {record.billing_period_start} to {record.billing_period_end}
                </TableCell>
                <TableCell align="right">
                  ${parseFloat(record.total_amount).toLocaleString()}
                </TableCell>
                <TableCell>
                  <Chip
                    label={record.status}
                    color={getStatusColor(record.status)}
                    size="small"
                  />
                </TableCell>
                <TableCell>{record.due_date || '-'}</TableCell>
                <TableCell>{record.paid_date || '-'}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
        <TablePagination
          component="div"
          count={billingData?.totalElements || 0}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </TableContainer>
    </Box>
  );
};

export default BillingRecords;
