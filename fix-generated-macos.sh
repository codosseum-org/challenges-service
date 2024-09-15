# Fix generated files after ktor update
LC_CTYPE=C && LANG=C  && grep -rl 'InternalAPI' build/ | xargs sed -i '' 's/io.ktor.util.InternalAPI/io.ktor.utils.io.InternalAPI/g'