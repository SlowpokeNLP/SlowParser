a heuristic based NLP parser
currently leveraging the Stanford Open NLP Parts of Speech and NER tagger libraries 
but hopefully eventually replacing both of those with homebrew components 

waiting on --
idiom resolution (similar to NER) 

plan is to create a parser to extract from raw text 'entities' with their attributes attached, and 'events'
with their 'entities' and adverbs attached.  the parser should do as well as it can without IRL knowledge.  later on 
to be married with some kind of big nosql json blob of taxonomical data that can be helped to disambiguate
in cases where grammar is not enough.  final step after the first two are complete is add sass mouth.  

called slow-poke because its an unfashionable, slow, lumbering imperative monster.  


