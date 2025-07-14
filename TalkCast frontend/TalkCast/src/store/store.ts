// src/redux/store.ts
import { configureStore } from '@reduxjs/toolkit';
import authReducer from '../slice/authSlice';

export const store = configureStore({
  reducer: {
    auth: authReducer,
  },
});

// Infer types
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
