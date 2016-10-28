#!/bin/bash

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_e12cfdc024de_key -iv $encrypted_e12cfdc024de_iv -in codesigning.asc.enc -out signingkey.asc -d
    gpg --fast-import signingkey.asc
fi
