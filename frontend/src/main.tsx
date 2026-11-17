import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { Toaster } from 'sonner';
import { CheckCircle2, AlertCircle, AlertTriangle, Info, Loader2 } from 'lucide-react';
import { store } from '@/store';
import App from './App';
import './index.css';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <Provider store={store}>
      <BrowserRouter>
        <App />
        <Toaster
          position="top-right"
          expand={false}
          closeButton
          duration={4000}
          richColors
          icons={{
            success: <CheckCircle2 className="w-5 h-5 text-emerald-500 shrink-0" />,
            error: <AlertCircle className="w-5 h-5 text-rose-500 shrink-0" />,
            warning: <AlertTriangle className="w-5 h-5 text-amber-500 shrink-0" />,
            info: <Info className="w-5 h-5 text-sky-500 shrink-0" />,
            loading: <Loader2 className="w-5 h-5 text-indigo-500 animate-spin shrink-0" />,
          }}
          toastOptions={{
            style: {
              zIndex: 99999,
            },
          }}
        />
      </BrowserRouter>
    </Provider>
  </React.StrictMode>
);
