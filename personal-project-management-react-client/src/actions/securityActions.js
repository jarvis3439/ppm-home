import axios from "axios";
import { GET_ERRORS, SET_CURRENT_USER } from "./types";
import setJwtToken from "../securityUtils/setJwtToken";
import jwt_decode from "jwt-decode";

export const createNewUser = (newUser, history) => async (dispatch) => {
  try {
    await axios.post("/user/register", newUser);
    history.push("/login");
    dispatch({
      type: GET_ERRORS,
      payload: {},
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data,
    });
  }
};

export const userLogin = (LoginRequest) => async (dispatch) => {
  try {
    // post => Login request
    const res = await axios.post("/user/login", LoginRequest);
    // extract token from res.data
    const { token } = res.data;
    // store token in local storage
    localStorage.setItem("jwtToken", token);
    // set our token in header **
    setJwtToken(token);
    // decode token in react
    const decoded = jwt_decode(token);
    // dispatch to our securityReducer

    dispatch({
      type: SET_CURRENT_USER,
      payload: decoded,
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data,
    });
  }
};

export const logout = () => (dispatch) => {
  localStorage.removeItem("jwtToken");
  setJwtToken(false);
  dispatch({
    type: SET_CURRENT_USER,
    payload: {},
  });
};
