function processSubmitDivs($) {
    $('.submit').click(function() {
        $(this).closest('form').submit();
    })
}