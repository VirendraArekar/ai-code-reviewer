import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import React from 'react';
import { Tabs } from '../components/ui/Tabs';

describe('Tabs Component', () => {
  const tabs = [
    { id: 'overview', label: 'Overview' },
    { id: 'findings', label: 'Findings', badge: 5 },
    { id: 'files', label: 'Files' },
  ];

  it('renders all tab labels and count badges', () => {
    render(<Tabs tabs={tabs} activeTab="overview" onChange={() => {}} />);

    expect(screen.getByText('Overview')).toBeDefined();
    expect(screen.getByText('Findings')).toBeDefined();
    expect(screen.getByText('5')).toBeDefined();
    expect(screen.getByText('Files')).toBeDefined();
  });

  it('fires onChange when a tab is clicked', () => {
    const handleChange = vi.fn();
    render(<Tabs tabs={tabs} activeTab="overview" onChange={handleChange} />);

    fireEvent.click(screen.getByText('Findings'));
    expect(handleChange).toHaveBeenCalledWith('findings');
  });
});
