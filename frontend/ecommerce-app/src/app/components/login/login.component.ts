import {Component, Inject, OnInit} from "@angular/core";
import {OktaAuth} from "@okta/okta-auth-js";

import AppConfig from "../../config/app-config";
import OktaSignIn from "@okta/okta-signin-widget";
import {OKTA_AUTH} from "@okta/okta-angular";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  oktaSignIn: any;

  constructor(@Inject(OKTA_AUTH) private oktaAuth: OktaAuth) {

    this.oktaSignIn = new OktaSignIn({
      logo: 'assets/images/logo.png',
      baseUrl: AppConfig.oidc.issuer.split('/oauth2')[0],
      clientId: AppConfig.oidc.clientId,
      redirectUri: AppConfig.oidc.redirectUri,
      authParams: {
        pkce: true,
        issuer: AppConfig.oidc.issuer,
        scopes: AppConfig.oidc.scopes
      }
    });
  }

  ngOnInit(): void {

    this.oktaSignIn.remove();

    this.oktaSignIn.renderEl({
      el: '#okta-sign-in-widget'},
      (response: any) => {
        if (response.status === 'SUCCESS') {
          this.oktaAuth.signInWithRedirect().then();
        }
      },
      (error: any) => {
        throw error;
      }
    );
  }

}
