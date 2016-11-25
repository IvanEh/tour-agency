$(function() {
    $('form').each(function(ind, el) {

        $el = $(el);
        $el.find('input[name=discount]').change((function($el) {
            return function() {
                $el.data('dirty', 'true');
            }
         })($el));
    });

    $('#save').click(function() {
        $('form').each(function(ind, form) {
            $form = $(form);
            if($form.data('dirty')) {
                $.post($form.attr('action'), $form.serialize());
            }
        });
    });
});