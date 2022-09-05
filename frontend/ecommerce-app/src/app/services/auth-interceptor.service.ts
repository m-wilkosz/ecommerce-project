import {Inject, Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {from, Observable} from "rxjs";
import {OKTA_AUTH} from "@okta/okta-angular";
import {OktaAuth} from "@okta/okta-auth-js";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return from(this.handleAccess(request, next));
  }

  private async handleAccess(request: HttpRequest<any>, next: HttpHandler): Promise<any> {

    // add an access token for secured endpoints
    const theEndpoint = environment.eCommerceApiUrl + '/orders';
    const securedEndpoints = [theEndpoint];

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
