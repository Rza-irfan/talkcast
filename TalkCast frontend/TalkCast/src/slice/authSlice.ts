import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";
import type { RootState } from "../store/store";

interface AuthState {
  accessToken: string;
}

const initialState: AuthState = {
  accessToken: "",
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setAccessToken: (state, action: PayloadAction<string>) => {
      console.log("Setting access token:", action.payload);
      state.accessToken = action.payload;
    }
    // clearAuth: (state) => {
    //   state.accessToken = "";
    // },
  },
});

export const { setAccessToken } = authSlice.actions;
export const selectAccessToken = (state: RootState) => state.auth.accessToken;
export default authSlice.reducer;
