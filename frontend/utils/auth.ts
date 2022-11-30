import config from "../config/config"
import { CognitoJwtVerifier } from 'aws-jwt-verify'

const getToken = () => {
  const hash = window.location.hash
  if (hash) {
    const token = hash.split('&')[0].split('=')[1]
    if (token) {
      localStorage.setItem('aygo-token-aws', token)
      return token
    } else {
      return localStorage.getItem('aygo-token-aws')
    }
  }
}

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
    localStorage.clear()
    console.log('Token is invalid', error)
    return false
  }
}

export default {getToken, validateToken}