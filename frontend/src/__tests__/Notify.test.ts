import { describe, it, expect, vi } from 'vitest';
import { notify } from '../lib/notify';
import { toast } from 'sonner';

vi.mock('sonner', () => ({
  toast: {
    success: vi.fn(),
    error: vi.fn(),
    warning: vi.fn(),
    info: vi.fn(),
    loading: vi.fn(),
    dismiss: vi.fn(),
  },
}));

describe('Notification Helper (notify)', () => {
  it('triggers toast.success with top-right configuration and colorful icon', () => {
    notify.success('Repository Connected', 'Successfully added repo.');
    expect(toast.success).toHaveBeenCalledWith(
      'Repository Connected',
      expect.objectContaining({
        description: 'Successfully added repo.',
        duration: 4000,
      })
    );
  });

  it('triggers toast.error with message and duration', () => {
    notify.error('Connection Failed', 'Repository already exists.');
    expect(toast.error).toHaveBeenCalledWith(
      'Connection Failed',
      expect.objectContaining({
        description: 'Repository already exists.',
        duration: 5000,
      })
    );
  });

  it('triggers toast.warning with title and options', () => {
    notify.warning('Caution', 'Exceeded diff limit.');
    expect(toast.warning).toHaveBeenCalledWith(
      'Caution',
      expect.objectContaining({
        description: 'Exceeded diff limit.',
        duration: 4500,
      })
    );
  });

  it('triggers toast.info with title', () => {
    notify.info('AI Review Started', 'Analyzing code changes...');
    expect(toast.info).toHaveBeenCalledWith(
      'AI Review Started',
      expect.objectContaining({
        description: 'Analyzing code changes...',
        duration: 4000,
      })
    );
  });
});
