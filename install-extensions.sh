
#!/bin/bash
cat << 'EOF' >.vscode/settings.json
{
    "extensions.ignoreRecommendations": true
}
EOF
cat << 'EOF' > .git/hooks/post-commit
#!/bin/bash
git push
git log -1 --shortstat > history_log.txt
EOF
chmod +x .git/hooks/post-commit
code --uninstall-extension revature-labs-non-prod.revature-angular-labs-non-prod && code --uninstall-extension hbenl.vscode-test-explorer && code --uninstall-extension ms-vscode.test-adapter-converter &&  code --uninstall-extension vscjava.vscode-java-test
code --install-extension redhat.java@1.35.1 && code --install-extension revaturePro.revature-labs