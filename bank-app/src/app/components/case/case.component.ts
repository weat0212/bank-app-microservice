import { Component, OnInit } from '@angular/core';
import { Case } from 'src/app/models/case-entity';

@Component({
  selector: 'app-case',
  templateUrl: './case.component.html',
  styleUrls: ['./case.component.css']
})
export class CaseComponent implements OnInit {

  id!: number;
  case!: Case;

  constructor(
     
  ) { }

  ngOnInit(): void {
  }

}
