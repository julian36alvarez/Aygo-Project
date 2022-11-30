import config from "../config/config"
import { CognitoJwtVerifier } from 'aws-jwt-verify'

const validateToken = async (token) => {
  const verifier = CognitoJwtVerifier.create({
    userPoolId: `${config.user_pool_id}`,
    tokenUse: 'id',
    clientId: `${config.cognito_client_id}`
  })

  try {
    console.log('Token', token)
    const payload = await verifier.verify(token)
    console.log('Token is valid. Payload', payload)
    return true
  } catch (error) {
    console.log('Token is invalid', error)
    return false
  }
}

const isAuth = async () => {
  const hash = window.location.hash
  if (hash) {
    const token = hash.split('&')[0].split('=')[1]
    let auth = false
    await validateToken(token).then((isValid) => {
      if (isValid) {
        auth = true
        localStorage.setItem('token', token)
      } else {
        console.log('Token is invalid')
      }
    })
    return auth
  } else {
    if (localStorage.getItem('token')) {
      let auth = false;
      await validateToken(localStorage.getItem('token')).then((isValid) => {
        if (!isValid) {
          localStorage.removeItem('token')
        } 
        auth = isValid
      })
      return auth
  }
}
}

export default {isAuth}