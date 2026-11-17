import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import React from 'react';
import { Badge } from '../components/ui/Badge';

describe('Badge Component', () => {
  it('renders severity badge correctly', () => {
    render(<Badge severity="CRITICAL" />);
    expect(screen.getByText('CRITICAL')).toBeDefined();
  });

  it('renders category badge correctly', () => {
    render(<Badge category="SECURITY" />);
    expect(screen.getByText('SECURITY')).toBeDefined();
  });
});
