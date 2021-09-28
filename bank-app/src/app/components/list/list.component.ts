import { Component, OnInit } from '@angular/core';
import { Case } from 'src/app/models/case-entity';
import { UserInfo } from 'src/app/models/user-entity';
import { CaseServiceService } from 'src/app/services/case-service.service';
import { UserDetailsService } from 'src/app/services/user-details.service';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {

  cases: Case[] | undefined;
  user!: UserInfo;

  constructor(
    private caseService: CaseServiceService,
    private userInfoService: UserDetailsService
  ) { }

  ngOnInit(): void {
    this.getUserInfo();
  }

  getUserCases(userId: string) {
    this.caseService.retrieveAllUserCases(userId).subscribe(
      res => {
        this.cases = res;
      }, 
      error => {
        console.log(error);
      }
    )
  }

  getUserInfo() {
    this.userInfoService.getUserDetailsByJwt().subscribe(
      res => {
        this.user = res
        console.log(this.user)
        this.getUserCases(this.user.id)
      }
    );
  }

  download(id:string) {
    this.caseService.downloadCase(id).subscribe(
      res => {
        console.log(res);
      }
    )
  }
}
