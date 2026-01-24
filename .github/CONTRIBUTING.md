# Contributing Guidelines

## GitHub Actions Workflow Guidelines

### Action Version References

When adding or updating GitHub Actions in workflow files (`.github/workflows/*.yml`), please follow these guidelines:

#### Use Version Tags (Preferred)

Use readable version tags instead of commit SHAs for better maintainability and readability:

```yaml
# ✅ Preferred: Use version tags
- uses: actions/checkout@v4
- uses: actions/setup-java@v4
- uses: DeLaGuardo/setup-clojure@12.5
```

```yaml
# ❌ Avoid: Commit SHAs are less readable
- uses: actions/checkout@b4ffde65f46336ab88eb53be808477a3936bae11 # v4
- uses: actions/setup-java@387ac29b308b003ca37ba93a6cab5eb57c8f5f93 # v4
```

#### Rationale

- **Readability**: Version tags are easier to understand at a glance
- **Maintainability**: Simpler to update and review changes
- **Clarity**: Clear indication of which version is being used

#### When to Update

- When a new major version of an action is released
- When security updates are available
- When new features are needed from a newer version

#### Testing

Before merging workflow changes:
1. Ensure YAML syntax is valid
2. Test the workflow on a branch to verify it works correctly
3. Review any workflow run logs for issues
