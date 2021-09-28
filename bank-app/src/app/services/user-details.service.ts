import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserInfo } from '../models/user-entity';

@Injectable({
  providedIn: 'root'
})
export class UserDetailsService {

  constructor(
    private http:HttpClient
  ) { }

  getUserDetailsByJwt() {
    return this.http.get<UserInfo>('http://localhost:8765/userinfo');
  }
}
