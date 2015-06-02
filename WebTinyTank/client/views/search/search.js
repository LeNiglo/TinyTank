Template.search.events({
    'submit form[role="search"]': function (e) {
        e.preventDefault();
        var query = $('input[name="search"]').val();

        //TODO add the check (if query is a tank, go to tank description)
        var isTank = false;

        if (isTank) {
            Router.go('tank', {id: query});
        } else {
            Router.go('profile', {_id: query});
        }
        //TODO correct the bug with Router.go profile if already on profile, simply reload withour window.location.
        // window.location.reload();
        return false;
    },
    'keyup input[name="search"]': function (e) {
        //TODO Play with url _GET ?q=
        var query = $('input[name="search"]').val();
    }
})
