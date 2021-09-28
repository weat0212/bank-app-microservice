import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Case } from '../models/case-entity';

@Injectable({
  providedIn: 'root'
})
export class CaseServiceService {

  constructor(
    private http: HttpClient
  ) { }

  retrieveCase(caseNum: string) {
    //TODO: NOT FINISHED
    return this.http.get('http://localhost:8765/filetrans/');
  }

  retrieveAllUserCases(userId: string) {
    return this.http.get<Case[]>("http://localhost:8765/filetrans/"+ userId +"/list");
  }

  downloadCase(caseNum: string) {
    return this.http.get('http://localhost:8765/filetrans/download/' + caseNum);
  }

  uploadCase(userId: string, caseNumber: string) {
    return this.http.post('http://localhost:8765/filetrans/' + {userId} + '/trans/' + {caseNumber}, {
      //TODO: body 
    })
  }
}