import {Component, Inject, OnInit} from "@angular/core";
import {OKTA_AUTH, OktaAuthStateService} from "@okta/okta-angular";
import {OktaAuth} from "@okta/okta-auth-js";

@Component({
  selector: 'app-login-status',
  templateUrl: './login-status.component.html',
  styleUrls: ['./login-status.component.css']
})
export class LoginStatusComponent implements OnInit {

  isAuthenticated: boolean = false;
  userFullName: string | undefined;

  constructor(private oktaAuthStateService: OktaAuthStateService,
              @Inject(OKTA_AUTH) private oktaAuth: OktaAuth) { }

  ngOnInit(): void {

    // subscribe to authentication state changes
    this.oktaAuthStateService.authState$.subscribe(
      (result) => {
        this.isAuthenticated = <boolean>result.isAuthenticated;
        this.getUserDetails();
      }
    );
  }

  getUserDetails() {
    if (this.isAuthenticated) {

      // fetch the logged-in user details (user's claims)

      // user full name is exposed as a property name
      this.oktaAuth.getUser().then(
        (res) => {
          this.userFullName = res.name;
        }
      );
    }
  }

  logout() {
    // terminates the session with Okta and removes current tokens
    this.oktaAuth.signOut().then();
  }
}
