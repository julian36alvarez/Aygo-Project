import {
  Box,
  Container,
  Flex,
  HStack,
  useColorModeValue,
  Link,
  IconButton,
  useDisclosure
} from '@chakra-ui/react'
import { CloseIcon, HamburgerIcon } from '@chakra-ui/icons'
import { RiGithubLine } from 'react-icons/ri'
import ThemeButton from './theme-button'
import HomeLink from './home-link'

const SimpleNavBar = () => {
  const { isOpen, onOpen, onClose } = useDisclosure()
  return (
    <Box
      as={'nav'}
      pos={'fixed'}
      width={'100%'}
      bg={useColorModeValue(
        'rgba(226, 232, 240, 0.9)',
        'rgba(23, 25, 35, 0.9)'
      )}
    >
      <Container maxW={'container.xl'} justifyContent={'space-between'}>
        <Flex h={16} alignItems={'center'} justifyContent={'space-between'}>
          <IconButton
            size={'md'}
            icon={isOpen ? <CloseIcon /> : <HamburgerIcon />}
            aria-label={'Open Menu'}
            display={{ md: 'none' }}
            onClick={isOpen ? onClose : onOpen}
          />
          <HStack spacing={8} alignItems={'center'}>
            <HomeLink>Video Stream &lt; S3</HomeLink>
          </HStack>
          <Flex alignItems={'center'}>
            <Link
              _focus={{ boxShadow: 'none' }}
              display={'inline-flex'}
              href={'https://www.github.com/julian36alvarez/Aygo-Project'}
            >
              <RiGithubLine size={20} />
              Source
            </Link>
            <ThemeButton />
          </Flex>
        </Flex>
      </Container>
    </Box>
  )
}

export default SimpleNavBar
