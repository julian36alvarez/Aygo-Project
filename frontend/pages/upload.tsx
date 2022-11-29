import {
  Container,
  Box,
  Heading,
  FormControl,
  FormLabel,
  Input,
  Button
} from '@chakra-ui/react'
import { useState } from 'react'
import Footer from '../components/footer'
import NavBar from '../components/navbar'
import config from '../config/config'

const UploadVideo = () => {
  const [description, setDescription] = useState('')
  const [file, setFile] = useState(null)

  const handleDescriptionChange = e => setDescription(e.target.value)
  const handleFileChange = e => setFile(e.target.files[0])

  const sendVideo = e => {
    e.preventDefault()
    const formData = new FormData()
    formData.append('file', file)
    fetch(`${config.url}/fileupload?description=${description}`, {
      method: 'POST',
      body: formData
    }).then(r => console.log(r))
  }

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
            <Input
              type={'text'}
              name="description"
              marginBottom={8}
              onChange={handleDescriptionChange}
            />
            <Input type="file" variant="unstyled" onChange={handleFileChange} />
            <Button
              type="submit"
              mt="4"
              colorScheme={'green'}
              onClick={sendVideo}
            >
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
