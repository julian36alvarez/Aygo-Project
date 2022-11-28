import { ChakraProvider } from '@chakra-ui/provider'
import Head from 'next/head'
import theme from '../themes/theme'

const App = ({ Component, pageProps }) => {
  return (
    <ChakraProvider theme={theme}>
      <Head>
        <title>Video Stream</title>
        <link rel="icon" href="/static/favicon.ico" type="image/x-icon"></link>
      </Head>
      <Component {...pageProps} />
    </ChakraProvider>
  )
}

export default App
