import {
  Container,
  Box,
  Heading,
  FormControl,
  FormLabel,
  Input,
  Button
} from '@chakra-ui/react'
import Footer from '../components/footer'
import NavBar from '../components/navbar'

const WatchVideo = () => {
  return (
    <Box pb={4}>
      <NavBar />
      <Container maxW={'container.md'} pt={20}>
        <Heading>Video Streaming from S3</Heading>
        <Container
          paddingTop={10}
          maxW={'container.sm'}
          paddingBottom={'800px'}
        >
          <video id="video1" width="750" height="440" controls>
            <source type="video/mp4" />
            Your browser does not support HTML video.
          </video>
        </Container>
      </Container>
      <Footer />
    </Box>
  )
}

export default WatchVideo
