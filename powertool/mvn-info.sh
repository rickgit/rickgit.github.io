
 curl --header "Content-Type:application/json"\
  "https://search.maven.org/solrsearch/select?q=a:guava&core=gav&start=0&rows=30" | \
  sed 's/[{},]/\n/g'|\
  grep -E  '^"id\"|Version\"'


curl --header "Content-Type:application/json"\
  "https://search.maven.org/solrsearch/select?q=a:hilt-android%20AND%20g:com.google.dagger&core=gav&start=0&rows=30" | \
  sed 's/[{},]/\n/g'|\
  grep -E  '^"id\"|Version\"'
