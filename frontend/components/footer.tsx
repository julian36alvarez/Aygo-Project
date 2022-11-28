import {
  ButtonGroup,
  Container,
  IconButton,
  Stack,
  Text,
  useColorMode
} from '@chakra-ui/react'
import { FaGithub, FaInstagram, FaLinkedin } from 'react-icons/fa'

const Footer = () => {
  const { colorMode, toggleColorMode } = useColorMode()
  return (
    <Container as={'footer'} py={16}>
      <Stack spacing={{ base: '4', md: '5' }}>
        <Stack justify={'space-between'} direction={'row'} align={'center'}>
          <Text
            fontSize={'sm'}
            color={colorMode === 'dark' ? 'whiteAlpha.600' : 'GrayText'}
          >
            &copy; {new Date().getFullYear()} AYGO
          </Text>
          <ButtonGroup variant={'ghost'}>
            <IconButton
              as={'a'}
              aria-label="LinkedIn"
              icon={<FaLinkedin fontSize="1.25rem" />}
            />
            <IconButton
              as={'a'}
              aria-label="GitHub"
              icon={<FaGithub fontSize="1.25rem" />}
            />
            <IconButton
              as={'a'}
              aria-label="Instagram"
              icon={<FaInstagram fontSize="1.25rem" />}
            ></IconButton>
          </ButtonGroup>
        </Stack>
      </Stack>
    </Container>
  )
}

export default Footer
