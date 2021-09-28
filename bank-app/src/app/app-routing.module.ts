import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { LogoutComponent } from './components/logout/logout.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { ErrorComponent } from './components/error/error.component';
import { RouteGuardService } from './services/route-guard.service';
import { ListComponent } from './components/list/list.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { FileTransferComponent } from './components/file-transfer/file-transfer.component';

const routes: Routes = [
  { path: "", component: LoginComponent },
  { path: "login", component: LoginComponent },
  { path: "registration", component: RegistrationComponent },
  { path: "welcome/:username", component: WelcomeComponent, canActivate: [RouteGuardService] },
  { path: "list", component: ListComponent, canActivate: [RouteGuardService] },
  { path: "filetrans", component: FileTransferComponent, canActivate: [RouteGuardService] },
  { path: "logout", component: LogoutComponent},
  { path: "**", component: ErrorComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
