import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import React from 'react';
import { Button } from '../components/ui/Button';

describe('Button Component', () => {
  it('renders children text properly', () => {
    render(<Button>Click Me</Button>);
    expect(screen.getByText('Click Me')).toBeDefined();
  });

  it('shows loading state and disables button when isLoading is true', () => {
    render(<Button isLoading>Submit</Button>);
    const btn = screen.getByRole('button');
    expect(btn).toBeDefined();
    expect(btn.getAttribute('disabled')).not.toBeNull();
  });
});
