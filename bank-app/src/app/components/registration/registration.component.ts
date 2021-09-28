import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { RegistrationService } from 'src/app/services/registration.service';


@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  submitted: boolean = false;
  form!: FormGroup;
  router: any;

  constructor(
    private registService: RegistrationService,
    private formBuilder: FormBuilder
  ) { }


  ngOnInit(): void {
    this.form = this.formBuilder.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]],
      terms: [false, [Validators.requiredTrue]],
    },
      { validator: this.checkIfMatchingPasswords('password', 'confirmPassword') } 
    )
  }

  // convenience getter for easy access to form fields
  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }

  public userRegistration() {
    // stop here if form is invalid
    if (this.form.invalid) {
      console.error('User Registration details not Complete')
      return;
    } else {
      this.validateAllFormFields(this.form);
      this.registService.register(this.form.value)
      this.submitted = true;
      console.warn('Your Registration has been submitted')
    }

  }

  validateAllFormFields(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach(field => {
      const control = formGroup.get(field);
      if (control instanceof FormControl) {
        control.markAsTouched({ onlySelf: true });
      } else if (control instanceof FormGroup) {
        this.validateAllFormFields(control);
      }
    });
  }

  onReset(): void {
    this.submitted = false;
    this.form.reset();
  }


  checkIfMatchingPasswords(passwordKey: string, passwordConfirmationKey: string) {
    return (group: FormGroup) => {
      let passwordInput = group.controls[passwordKey],
        passwordConfirmationInput = group.controls[passwordConfirmationKey];
      if (passwordInput.value !== passwordConfirmationInput.value) {
        passwordConfirmationInput.setErrors({ notEquivalent: true })
        return false
      }
      else {
        passwordConfirmationInput.setErrors(null)
        return true;
      }
    }
  }
}
