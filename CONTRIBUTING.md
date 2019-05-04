# Contributing Guide

### Getting started
First things first, you're going to want to clone the repository and then `cd` into it:

```sh
git clone https://github.com/mdpoulter/nis-prac.git
cd nis-prac
```

### Adding a feature
To get started on adding a new feature, first checkout and pull master:

```sh
git checkout master
git pull origin master
```

Then create a new branch describing your feature:

```sh
git checkout -b feature/feature-name
```

While developing, remember to commit regularly and use appropriate commit messages:

```sh
git add .                           # or git add <file>
git commit -m "Commit message"
```

You can also push it to the remote repository to keep your branch backed up. The first time you do this, you will need to run:

```sh
git push -u origin feature/feature-name
```

For future pushes however, you can simply run:

```sh
git push origin
```

Finally, once your feature is complete, all the tests are still passing, and you have pushed to origin, you can create a pull request on Github.

Navigate to the repository on Github and you’ll see that your branch should be listed at the top with a useful "Compare & pull request" button. Click it! Make sure to set the base to `master` and provide a little bit of context in the pull request before submitting it.

Once submitted, the pull request (currently) requires approval from 2 other contributors before it can be merged into the `master` branch so let us know on the WhatsApp group to get on that.

### Fixing a big

Bug fixes can be done in almost exactly the same way, however please name your branches accordingly:

```sh
git checkout -b bugfix/bugfix-name
```

### Changes to this guide

Feel free to make any changes to this guide that you see fit and submit a pull request for it.
