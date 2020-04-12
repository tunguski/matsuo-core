#!/bin/bash

# CI_PROJECT_ID

HOST=http://gitlab.matsuo.pl

#PROJECT_ID=$(curl -H "PRIVATE-TOKEN: $USER_PRIVATE_TOKEN" $HOST/api/v4/projects | python -m json.tool | grep -E "gitlab_id|\"id\"" | grep "id\": $CI_PROJECT_ID," -B 1 | grep "\"gitlab_id\"" | cut -d ":" -f 2 | cut -d "," -f 1 | sed -e 's/^[[:space:]]*//')
COMMIT_ID=$CI_BUILD_REF

CHECKSTYLE=$(for i in `find -name checkstyle-result.xml`; do cat $i; done | grep "<error " | wc -l)
PMD=$(for i in `find -name pmd.xml`; do cat $i; done | grep "<violation" | wc -l)
FIND_BUGS=$(for i in `find -name findbugsXml.xml`; do cat $i | tr "<" "\n" | grep "^FindBugsSummary" | tr " " "\n" | grep total_bugs | cut -d "'" -f 2; done | awk '{s+=$1} END {print s}')


URL=$HOST/api/v4/projects/$CI_PROJECT_ID/repository/commits/$COMMIT_ID/comments

echo $URL

read -r -d '' NOTE << EOM
# Build result

| Type | Result |
| ----: | ----- |
| **Checkstyle** | $CHECKSTYLE |
| **Pmd** | $PMD |
| **Find Bugs** | $FIND_BUGS |
| **Cobertura** | |
EOM

for i in `cat target/site/cobertura/coverage.xml | grep lines-covered | cut -d " " -f 2- | sed 's/ /\n/g'`
do 
  TMP=$(echo $i | sed 's/[">]//g' | sed 's/=/ \| /g' | sed 's/^/\| /g' | sed 's/$/ \|/g')
  TMP+=$'\n'
  NOTE="$NOTE$TMP"
done

curl --data $"note=$NOTE" \
    -H "PRIVATE-TOKEN: $USER_PRIVATE_TOKEN" \
    $URL