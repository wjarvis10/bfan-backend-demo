package com.example.bfandemo.server.httpResponses;

public class GetResponse {
  public boolean success;
  public Object object;

  public GetResponse(boolean success, Object data) {
    this.success = success;
    this.object = data;
  }

}
