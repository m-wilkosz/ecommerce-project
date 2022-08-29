export default {

  oidc: {
    clientId: '0oa69nl8v5ubRu6hi5d7',
    issuer: 'https://dev-72782908.okta.com/oauth2/default',
    redirectUri: window.location.origin + '/login/callback',
    scopes: ['openid', 'profile', 'email']
  }
}
