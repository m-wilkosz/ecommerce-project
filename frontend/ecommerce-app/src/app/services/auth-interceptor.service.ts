import {Inject, Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {from, Observable} from "rxjs";
import {OKTA_AUTH} from "@okta/okta-angular";
import {OktaAuth} from "@okta/okta-auth-js";

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return from(this.handleAccess(request, next));
  }

  private async handleAccess(request: HttpRequest<any>, next: HttpHandler): Promise<any> {

    // add an access token for secured endpoints
    const securedEndpoints = ['http://localhost:8080/api/orders'];

    if (securedEndpoints.some(url => request.urlWithParams.includes(url))) {

      // get access token
      const accessToken = await this.oktaAuth.getAccessToken();

      // clone the request and add new header with access token
      request = request.clone({
        setHeaders: {
          Authorization: 'Bearer ' + accessToken
        }
      });
    }

    return next.handle(request).toPromise();
  }

  constructor(@Inject(OKTA_AUTH) private oktaAuth: OktaAuth) { }
}
