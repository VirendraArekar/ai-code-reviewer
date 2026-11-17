import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import React from 'react';
import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card';

describe('Card Component', () => {
  it('renders card title and children', () => {
    render(
      <Card>
        <CardHeader>
          <CardTitle>System Metrics</CardTitle>
        </CardHeader>
        <CardContent>
          <p>Active repositories: 12</p>
        </CardContent>
      </Card>
    );

    expect(screen.getByText('System Metrics')).toBeDefined();
    expect(screen.getByText('Active repositories: 12')).toBeDefined();
  });
});
