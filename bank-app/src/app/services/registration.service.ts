import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user-entity';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(
    private http:HttpClient,
    private router: Router
  ) { }

  register(user: User) {
    this.http.post('http://localhost:8765/registration', user)
    .subscribe(
      data => {
        this.router.navigate(['/login'])
      },
      error => {
        console.error(error)
      }
    )
  }
}
