import React, { useState } from 'react';
import { Outlet } from 'react-router-dom';
import { Sidebar } from '@/components/navigation/Sidebar';
import { Navbar } from '@/components/navigation/Navbar';
import { cn } from '@/lib/utils';

export const MainLayout: React.FC = () => {
  const [collapsed, setCollapsed] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col">
      <Sidebar
        collapsed={collapsed}
        onToggle={() => setCollapsed(!collapsed)}
        mobileOpen={mobileOpen}
        onMobileClose={() => setMobileOpen(false)}
      />

      <div className="flex-1 flex flex-col">
        <Navbar
          onMobileOpen={() => setMobileOpen(true)}
          collapsed={collapsed}
        />

        <main
          className={cn(
            'flex-1 transition-all duration-200 p-4 sm:p-6 lg:p-8',
            collapsed ? 'lg:pl-24' : 'lg:pl-72'
          )}
        >
          <div className="max-w-7xl mx-auto space-y-8 animate-fade-in">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
};
