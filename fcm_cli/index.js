const {JWT} = require('google-auth-library');

function getAccessToken() {
  return new Promise(function(resolve, reject) {
    const key = require('./secret/otameshi-bati11-7d768b9764b5.json');
    const jwtClient = new JWT(
      key.client_email,
      null,
      key.private_key,
      ['https://www.googleapis.com/auth/cloud-platform'],
      null
    );
    jwtClient.authorize(function(err, tokens) {
      if (err) {
        reject(err);
        return;
      }
      resolve(tokens.access_token);
    });
  });
}

(async () => {
  const token = await getAccessToken()
  console.log(token)
})()

