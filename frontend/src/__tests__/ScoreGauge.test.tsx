import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import React from 'react';
import { ScoreGauge } from '../components/ui/ScoreGauge';

describe('ScoreGauge Component', () => {
  it('renders score and label correctly', () => {
    render(<ScoreGauge score={88} label="Quality Score" />);
    expect(screen.getByText('88')).toBeDefined();
    expect(screen.getByText('Quality Score')).toBeDefined();
  });

  it('renders perfect score without crashing', () => {
    render(<ScoreGauge score={100} label="Security Score" />);
    expect(screen.getByText('100')).toBeDefined();
  });
});
