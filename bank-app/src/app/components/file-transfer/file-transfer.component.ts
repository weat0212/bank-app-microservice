import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CaseServiceService } from 'src/app/services/case-service.service';

@Component({
  selector: 'app-file-transfer',
  templateUrl: './file-transfer.component.html',
  styleUrls: ['./file-transfer.component.css'],
  providers: [DatePipe]
})
export class FileTransferComponent implements OnInit {

  myDate: Date;
  form!: FormGroup;
  submitted: boolean = false;

  constructor(private datePipe: DatePipe,
    private caseService: CaseServiceService,
    private formBuilder: FormBuilder
  ) {
    this.myDate = new Date();
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      caseNum: ['', [Validators.required]],
      emiNum: ['', [Validators.required]],
      creditCardNum: ['', [Validators.required, Validators.email]],
      customerName: ['', [Validators.required]]
    })
  }

  // convenience getter for easy access to form fields
  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

  uploadForm() {
    if(this.form.invalid) {
      console.error('Not Completed')
      return;
    } else {
      // this.caseService.uploadCase(this.form.value)
      this.submitted = true;
    }
  }

  onReset(): void {
    this.submitted = false;
    this.form.reset();
  }
}
