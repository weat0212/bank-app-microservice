import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username = ''
  password = ''

  errorMessage = '使用者登入資料錯誤，請重新輸入'
  invalidLogin!: boolean

  // Router
  // Angular.giveMeRouter
  // Dependency Injection
  
  constructor(private router: Router, private auth:LoginService) { }

  ngOnInit(): void {
    this.invalidLogin = false;
  }

  handleLogin() {
    this.auth.login(this.username, this.password)
    if(this.auth.isLoggedIn()) {
      //Redirect to Welcome Page
      this.router.navigate(['welcome', this.username])
      this.invalidLogin = false
    } else {
      this.invalidLogin = true
    }
  }
}
