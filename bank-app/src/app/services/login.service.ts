import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Injectable } from '@angular/core';
import * as moment from "moment";


const TOKEN = 'id_token';
const EXPIRED = "expires_at";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  
  constructor(
    private http: HttpClient
  ) {}

  // For user authentication
  login(username: string, password: string) {

    let body = new URLSearchParams();
    body.set('username', username);
    body.set('password', password);

    this.http.post<any>('http://localhost:8765/login/process', body, {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded'),
      observe: 'response',
      responseType: "json"
    }).subscribe(
      res => {
        // Store JWT token
        let token = res.headers.get('Authorization');
        localStorage.setItem(TOKEN, "Bearer " + token!);
      }
    )
  }

  // private setSession(authResult: { expiresIn: any; idToken: string; }) {
  //   const expiresAt = moment().add(authResult.expiresIn, 'second');
  //   localStorage.setItem(EXPIRED, JSON.stringify(expiresAt.valueOf()));
  // }

  logout() {
    localStorage.removeItem(TOKEN);
    localStorage.removeItem(EXPIRED);
  }

  public isLoggedIn() {
    if(localStorage.getItem(TOKEN) === null) {
      return false;
    } else {
      return true;
    }
    // return moment().isBefore(this.getExpiration());
  }

  isLoggedOut() {
    return !this.isLoggedIn();
  }

  getExpiration() {
    const expiration: string | any = localStorage.getItem(EXPIRED);
    const expiresAt = JSON.parse(expiration);
    return moment(expiresAt);
  }
}
