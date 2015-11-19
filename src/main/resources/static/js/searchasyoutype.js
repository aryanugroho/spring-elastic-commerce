$(document).ready(function() {
  var terms,products, remoteHost, template_terms,template_products;

  $.support.cors = true;

  remoteHost = 'http://localhost:8080';
  template_products = Handlebars.compile($("#result-template-products").html());
  template_terms = Handlebars.compile($("#result-template-terms").html());
  
  terms = new Bloodhound({
    identify: function(o) { return o.id_str; },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('term'),
    dupDetector: function(a, b) { return a.id_str === b.id_str; },
    remote: {
      url: remoteHost + '/api/terms/suggest/%QUERY',
      wildcard: '%QUERY'
    }
  });

  
  function termsWithDefaults(q, sync, async) {
    terms.search(q, sync, async);
  }

  products = new Bloodhound({
    identify: function(o) { return o.id_str; },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('title', 'id'),
    dupDetector: function(a, b) { return a.id_str === b.id_str; },
    remote: {
      url: remoteHost + '/api/products/suggest/%QUERY',
      wildcard: '%QUERY'
    }
  });

  
  function productsWithDefaults(q, sync, async) {
    products.search(q, sync, async);
  }

  $('#demo-input').typeahead({
    hint: $('.Typeahead-hint'),
    menu: $('.Typeahead-menu'),
    minLength: 1,
    autoSelect: true,
    classNames: {
      open: 'is-open',
      empty: 'is-empty',
      cursor: 'is-active',
      suggestion: 'Typeahead-suggestion',
      selectable: 'Typeahead-selectable'
    }
  }, {
    source: termsWithDefaults,
    displayKey: 'term',
    templates: {
      suggestion: template_terms
    }
  },{
    source: productsWithDefaults,
    displayKey: 'title',
    templates: {
      suggestion: template_products
    }
  })
  .on('typeahead:asyncrequest', function() {
    $('.Typeahead-spinner').show();
  })
  .on('typeahead:asynccancel typeahead:asyncreceive', function() {
    $('.Typeahead-spinner').hide();
  }).on('typeahead:selected', function (event, data) {    
    if(data.productUrl) {
      window.location = data.productUrl;
    } else if(data.term) {
      window.location = '/products/search/' + data.term + '*';
    }
});

});