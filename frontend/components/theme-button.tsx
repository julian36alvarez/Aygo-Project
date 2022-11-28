import { Box, Button } from '@chakra-ui/react'
import { useColorMode } from '@chakra-ui/react'
import { MoonIcon, SunIcon } from '@chakra-ui/icons'

const ThemeButton = () => {
  const { colorMode, toggleColorMode } = useColorMode()
  return (
    <Box>
      <Button margin={4} onClick={toggleColorMode}>
        {colorMode === 'light' ? <MoonIcon /> : <SunIcon />}
      </Button>
    </Box>
  )
}

export default ThemeButton
