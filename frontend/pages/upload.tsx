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

const UploadVideo = () => {
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
          <FormControl>
            <FormLabel>Video Description</FormLabel>
            <Input required type={'text'} name="description" marginBottom={8} />
            <Input type="file" variant="unstyled" />
            <Button type="submit" mt="4" colorScheme={'green'}>
              Upload
            </Button>
          </FormControl>
        </Container>
      </Container>
      <Footer />
    </Box>
  )
}

export default UploadVideo
