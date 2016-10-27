#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_2b49c16e4094_key -iv $encrypted_2b49c16e4094_iv -in codesigning.asc.enc -out codesigning.asc -d
    gpg --fast-import signingkey.asc
fi

echo "before-deploy.sh"